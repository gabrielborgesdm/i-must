import { Router } from "express";
import { TodoController } from "@controllers/TodoController";

const router = Router();
const todo = new TodoController();

router.post('/insert', (request, response) => {
    return todo.insertTodos(request, response);
});

export { router }