import { Router } from 'express'
import { TaskController } from '@controllers/TaskController'

import ValidationMiddleware from '@middlewares/ValidationMiddleware'
import { TaskIdSchema, TasksSchema } from '@models/schemas/TaskSchemas'
import { SignUpSchema, SignInSchema } from '@models/schemas/UserSchema'
import { UserController } from '@controllers/UserController'
import AuthMiddleware from '@middlewares/AuthMiddleware'

const router = Router()
const task = new TaskController()
const user = new UserController()
const validation = new ValidationMiddleware()
const auth = new AuthMiddleware()

router.post('/signup', validation.validate(SignUpSchema, 'user'), (request, response) => {
  return user.signUp(request, response)
})

router.post('/signin', validation.validate(SignInSchema, 'user'), (request, response) => {
  return user.signIn(request, response)
})

router.all('/*', auth.authenticateToken)

router.post('/tasks', validation.validate(TasksSchema, 'tasks'), (request, response) => {
  return task.upsertMany(request, response)
})

router.get('/tasks', (request, response) => task.getAll(request, response))

router.get('/task/:id', validation.validate(TaskIdSchema, 'id', 'params'), (request, response) => {
  return task.get(request, response)
})

router.delete('/task/:id', validation.validate(TaskIdSchema, 'id', 'params'), (request, response) => {
  return task.delete(request, response)
})

export { router }
