import { Request, Response } from "express";

<<<<<<< HEAD
import Todo from '@models/Todo'
import TodoJoi from '@models/TodoJoi'
import { ValidationError } from 'joi'

export class TodoController {
  async insertMany (request: Request, response: Response): Promise<Response> {
    const { tasks } = request.body
    const errors: Array<ValidationError> = []
    if (!tasks || tasks.length === 0) {
      return response.status(200).json({ success: false, error: "Tasks can't be empty." })
    }
    tasks.forEach((task:any) => {
      const { error } = TodoJoi.validate(task)
      if (error) errors.push(error)
    })
=======

export class TodoController {
>>>>>>> parent of 5eed574... Joi Validation, Mongoose, Insert MUltiple tasks

    async insertTodos(request: Request, response: Response): Promise<Response> {

<<<<<<< HEAD
    return response.status(200).json({ success: true })
  }

  async getAll (request: Request, response: Response): Promise<Response> {
    let tasks: Array<any> = []
    let error: any = false
    try {
      await Todo.find({}, (err, docs) => {
        if (err) error = err
        else tasks = docs
      })
    } catch (error) {
      console.log(error)
    }
    if (error) {
      console.log(error)
      return response.status(200).json({ success: false, message: "It wasn't possible to get tasks.", error: error.name })
    } else {
      return response.status(200).json({ success: true, tasks })
    }
  }

  async get (request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    let task: any = false
    let error: any = false
    try {
      await Todo.findOne({ id: id }, (err:any, doc: typeof Todo) => {
        if (err) error = err
        else task = doc
      })
    } catch (error) {
      console.log(error)
    }
    if (error) {
      console.log(error)
      return response.status(200).json({ success: false, message: "It wasn't possible to get task.", error: error.name })
    } else if (!task) {
      return response.status(200).json({ success: false, message: 'There is no task with this \'id\'' })
    } else {
      return response.status(200).json({ success: true, task })
    }
  }
}
=======
        return response.status(200).json({ "success": true });
    }
}
>>>>>>> parent of 5eed574... Joi Validation, Mongoose, Insert MUltiple tasks
