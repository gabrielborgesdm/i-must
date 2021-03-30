import { Request, Response } from "express";


export class TodoController {

    async insertTodos(request: Request, response: Response): Promise<Response> {

        return response.status(200).json({ "success": true });
    }
}