import { Router } from "express";
import { TodoController } from "@controllers/TodoController";

const router = Router();
const todo = new TodoController();

router.post('/insert', (request, response) => {
    return todo.insertTodos(request, response);
});

<<<<<<< HEAD
router.get('/task/:id', (request, response) => {
  return todo.get(request, response)
})

router.get('/tasks', (request, response) => {
  return todo.getAll(request, response)
})

export { router }
=======
export { router }
>>>>>>> parent of 5eed574... Joi Validation, Mongoose, Insert MUltiple tasks
