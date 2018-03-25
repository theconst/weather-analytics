import { ConnectionOptions } from "mysql2/promise";
import * as mysql from "mysql2/promise";
export const connectionPool: mysql.Pool = mysql.createPool({
    connectionLimit: Number.parseInt(process.env.POOL_SIZE) || 5,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DATABASE || "kiev_weather_data",
    host: process.env.DBA_HOST || "localhost",
    port: Number.parseInt(process.env.DB_PORT) || 3306,
});

