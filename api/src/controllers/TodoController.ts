import { Request, Response } from 'express'

import Todo from '@models/Todo'
import TodoJoi from '@models/TodoJoi'
import { InsertManyResult } from 'mongoose'
import { ValidationError } from 'joi'

export class TodoController {
  async insertMany (request: Request, response: Response): Promise<Response> {
    const { tasks } = request.body
    const errors: Array<ValidationError> = []
    if (!tasks || tasks.length == 0) {
      return response.status(200).json({ success: false, error: "tasks can't be empty" })
    }
    tasks.forEach((task:any) => {
      const { error } = TodoJoi.validate(task)
      if (error) errors.push(error)
    })

    if (errors.length > 0) {
      return response.status(200).json({ success: false, errors })
    }
    try {
      await Todo.insertMany(tasks)
    } catch (error) {
      console.log(error)
    }

    return response.status(200).json({ success: true })
  }
}
