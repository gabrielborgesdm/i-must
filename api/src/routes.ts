import { Router } from 'express'
import { TaskController } from '@controllers/TaskController'

import ValidationMiddleware from '@middlewares/ValidationMiddleware'
import { TaskIdSchema, TasksSchema } from '@middlewares/schemas/TaskSchemas'

const router = Router()
const todo = new TaskController()
const validation = new ValidationMiddleware()

router.post('/tasks', validation.validate(TasksSchema, 'tasks'), (request, response) => {
  return todo.upsertMany(request, response)
})

router.get('/tasks', (request, response) => todo.getAll(request, response))

router.get('/task/:id', validation.validate(TaskIdSchema, 'id', 'params'), (request, response) => {
  return todo.get(request, response)
})

router.delete('/task/:id', validation.validate(TaskIdSchema, 'id', 'params'), (request, response) => {
  return todo.remove(request, response)
})

export { router }
