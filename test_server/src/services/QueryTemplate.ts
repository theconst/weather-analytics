import { connectionPool } from "../config/dbConfig";
import { RowDataPacket, OkPacket, FieldPacket } from "mysql2/promise";
import logger from "../util/logger";
export class QueryTemplate<T> {

    private _mapper: (q: RowDataPacket[]) => T;

    private _query: string;
    constructor(query: string, theMapper: ((q: RowDataPacket[]) => T)) {
        this._mapper = theMapper;
        this._query = query;
    }

    async execute(...params: (Date | string | number)[]): Promise<T> {
        const resultSet = await connectionPool.query(this._query, params);
        return this._mapper(<RowDataPacket[]>resultSet[0]);
    }

}