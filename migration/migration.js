/*
 * migration.js
 * Migrates xslx files to the database
 */
'use strict';

const fs = require('fs');
const path = require('path');
const Rx = require('rxjs');
const glob = require('glob');
const mysql = require('mysql2/promise');
const xlsx = require('xlsx');
const helpers = require('./helpers.js');

/**
 * Function that migrates to the database asynchron
 * @argument config - file configurations configuration options
 * @argument options - connection and cleanup file cleanup options
 */
async function migrate(config, options) {
    const dataPath = path.join(options.files, config.dataPattern);
    console.debug(`Importing files from ${dataPath}`);
    //file with merged xslts, verbatim
    const mergedFile = `${helpers.replaceGlob(dataPath, config.dataSuffix)}.csv`;
    let connection;
    try {
        connection = await mysql.createConnection({
            host: options.host,
            user: options.user,
            password: options.password,
            port: options.port
        });
        console.debug('Connection established...');
    
        await connection.beginTransaction();
    
        if (options.bounce) {
            console.debug('Dropping database...');
            await connection.query(
                `DROP DATABASE IF EXISTS ${config.databaseName}`);
        }
    
        console.debug('Creating database...');
        await connection.query(
            `CREATE DATABASE IF NOT EXISTS ${config.databaseName};`);
    
        console.debug('Using database...');
        await connection.query(`USE ${config.databaseName} `);
    
        console.debug('Creating table...');       
        const fields = Object.entries(config.typeMapping)
            .map(([name, type]) => 
                `\`${name}\` ${type}${(name == config.idField) 
                    ? ' PRIMARY KEY AUTO_INCREMENT' : 
                    ''}`)
            .join(',');
        await connection.query(
            `CREATE TABLE IF NOT EXISTS \`${config.tableName}\`(${fields});`);
    
        const countQuery = `SELECT COUNT(*) FROM ${config.tableName}`;
        const [[{ 'COUNT(*)': countBefore }]] = await connection.query(countQuery);
        console.debug(`${countBefore} items in total`);
    
        await Rx.Observable.bindNodeCallback(glob)(dataPath)
            .map(helpers.sortByNumber(config.dataPattern))
            .mergeMap(helpers.cacheToCsvFile(mergedFile))
            .mergeMap(helpers.toLines)
            .let(helpers.zipWithFirst())
            .filter(([heading, line]) => heading != line)
            .map(([heading, line]) => [heading.split(','), line.split(',')])
            .map(helpers.zipToObj)
            .bufferCount(config.batchSize)
            .map(helpers.toBatchInsert(config.tableName, config.typeMapping))
            .map(connection.query.bind(connection))
            .reduce((p1, p2) => Promise.all([p1, p2]), Promise.resolve())
            .toPromise();
    
        console.debug('Commiting successfully...');
        await connection.commit();
    
        const [[{ 'COUNT(*)': countAfter }]] = await connection.query(countQuery);
        const count = countAfter - countBefore;
    
        console.debug(`${count} items successfully imported`);
        console.debug(`${countAfter} items in total`);
    
        return 0;       //migration success
    } catch (error) {
        console.error(error);
        console.debug('Rolling back transaction...');
        await connection.rollback();
    
        return 1;       //migration failure
    } finally {
        console.debug('Closing connection...');
        if (connection) {
            await connection.close();
    
            if (options.cleanup && fs.existsSync(mergedFile)) {
                console.debug('Cleaning up...');
                fs.unlinkSync(mergedFile);
            }
        }
    }
}

module.exports = {
    migrate : migrate
}