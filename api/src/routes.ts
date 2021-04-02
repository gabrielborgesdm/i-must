import { Router } from 'express'
import { TodoController } from '@controllers/TodoController'

const router = Router()
const todo = new TodoController()

router.post('/tasks', (request, response) => {
  return todo.insertMany(request, response)
})

router.get('/task/:id', (request, response) => {
  return todo.get(request, response)
})

router.get('/tasks', (request, response) => {
  return todo.getAll(request, response)
})

export { router }
