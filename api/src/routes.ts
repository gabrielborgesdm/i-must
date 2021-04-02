import { Router } from 'express'
import { TaskController } from '@controllers/TaskController'

const router = Router()
const todo = new TaskController()

router.post('/tasks', (request, response) => {
  return todo.upsertMany(request, response)
})

router.get('/task/:id', (request, response) => {
  return todo.get(request, response)
})

router.get('/tasks', (request, response) => {
  return todo.getAll(request, response)
})

export { router }
