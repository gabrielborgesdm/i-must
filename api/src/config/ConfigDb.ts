import oracledb from 'oracledb';

export class Database {
    private cns: Object = {
        user: process.env.DATABASE_USER,
        password: process.env.DATABASE_PASSWORD,
        connectString: process.env.DATABASE_URL
    }

    async Open(sql: string, binds: any, autoCommit: any) {
        let cnn = await oracledb.getConnection(this.cns);
        let result = await cnn.execute(sql, binds, { autoCommit });
        cnn.release();
        return result;
    }
}