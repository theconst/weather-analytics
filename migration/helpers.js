/* 
 *  helpers.js
 *  Helper functions for the migration script
 */
'use strict';

const fs = require('fs');
const Rx = require('rxjs');
const glob = require('glob');
const xlsx = require('xlsx');
const readline = require('readline');

const zipWithFirst = (zipper = (first, data) => [first, data]) => source => {
    let first;
    return Rx.Observable.create(observer => {
        return source.subscribe(data => {
            try {
                if (!first) {
                    first = data;
                } else {
                    observer.next(zipper(first, data));
                }
            } catch (error) {
                observer.error(error);
            }
        },
            observer.error.bind(observer),
            observer.complete.bind(observer));
    });
};

const replaceGlob = (glob, replacement) => glob.replace(/\*+/, replacement);

const extractNumberFromFileName = (glob, fName) => {
    const match = new RegExp(replaceGlob(glob, '(.*)')).exec(fName);
    const number = match[1];
    if (!number || match.length > 2) {
        throw new Error(`No number found by '${regex}' in '${fName}'`);
    }
    return number;
};

const sortByNumber = pattern => files => {
    return files.sort((fn1, fn2) =>
        [fn1, fn2]
            .map(fName => extractNumberFromFileName(pattern, fName))
            .reduce((n1, n2) => n1 - n2));
};

const cacheToCsvFile = fileName => files => {
    return Rx.Observable.create(observer => {
        const concatenatedFile = fs.createWriteStream(fileName);

        Rx.Observable.of(...files)
            .map(file => xlsx.readFile(file))
            .mergeMap(workbook => workbook.SheetNames
                .map(name => workbook.Sheets[name]))
            .map(sheet => xlsx.utils.sheet_to_csv(sheet))
            .forEach(csv => concatenatedFile.write(csv))
            .then(() => {
                concatenatedFile.end();
            });
        concatenatedFile
            .once('error', err => observer.error(err))
            .once('finish', () => {
                observer.next(fileName);
                observer.complete();
            });
    });
};

const toLines = fileName => {
    return Rx.Observable.create(observer => {
        const concatenatedCsv = fs.createReadStream(fileName);
        const lines = readline.createInterface({
            input: concatenatedCsv,
            crlfDelay: Infinity
        });
        concatenatedCsv.on('error', error => {
            observer.error(error);
            observer.complete();
        });
        lines.on('line', line => observer.next(line));
        lines.on('close', () => observer.complete())
    });
};

const zipToObj = ([keys, values]) => {
    if (keys.length !== values.length) throw new Error('length of arrays do not match');

    let obj = {};
    keys.forEach((key, i) => obj[key] = values[i]);
    return obj;
};

const parseValue = typeMapping => (key, value) => {
    const type = typeMapping[key];
    let result = value;

    //TODO: handle more types, they weren't messed up though
    if (type.includes('DOUBLE')) {
        result = Number.parseFloat(value);
    } else if (type.includes('INT')) {
        result = Number.parseInt(value);
    }

    //TODO: handle without side effects
    if (typeof result === 'undefined') {
        console.warn(`Ivalid value ${value} for type ${type} in ${key}`);
    }

    return result ? `'${result}'` : 'null';
};

const toBatchInsert = (tableName, typeMapping) => {
    const parse = parseValue(typeMapping);
    return batch => {
        const keys = Object.keys(batch[0]);
        const heading = keys.map(h => `\`${h}\``);
        const values = batch
            .map(entry => `(${keys.map(key => parse(key, entry[key])).join(',')})`)
            .join(',');

        return `INSERT INTO ${tableName}(${heading}) VALUES ${values};`;
    };
};

module.exports = {
    zipWithFirst: zipWithFirst,
    replaceGlob: replaceGlob,
    sortByNumber: sortByNumber,
    cacheToCsvFile: cacheToCsvFile,
    toLines: toLines,
    zipToObj: zipToObj,
    toBatchInsert: toBatchInsert,
};