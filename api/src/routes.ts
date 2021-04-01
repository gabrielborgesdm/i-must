import { Router } from 'express'
import { TodoController } from '@controllers/TodoController'

const router = Router()
const todo = new TodoController()

router.post('/tasks', (request, response) => {
  return todo.insertMany(request, response)
})

export { router }
