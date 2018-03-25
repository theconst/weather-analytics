/**
 * launcher.js
 * Launches migration script using command line
 */

const program = require('commander');
const config = require('./config.json');
const migration = require('./migration.js');

program.version('1.0.0', '-v, --version')
.description(
    `Script that migrates xlsx files to MYSQL database table.
    To configure database and table name, type mappings and file names, 
    tune the config.json file parameters.
    If a datatype cannot be converted to the one specified in the type mapping, 
    null value is written to the database.
    Required options: -u, -p.
    Optional opiyions: -h, -n, -c, -b.`)
.usage('<options>')
.option('-h, --host <host>', 'Database host', 'localhost')
.option('-p, --password <password>', 'Database password')
.option('-u, --user <user>', 'Database user name')
.option('-n, --port <port>', 'Database port', 3306)
.option('-c, --cleanup', 'Cleanup generated files')
.option('-b, --bounce', 'Drop database before migration')
.option('-f, --files <path>', 'Path to the files for migration')
.parse(process.argv);

migration.migrate(config, program)
    .then(status => process.exit(status))
    .catch(() => process.exit(127)); // unknown error