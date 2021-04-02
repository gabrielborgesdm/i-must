import { Request, Response } from 'express'

import Task from '@models/Task'
import TaskJoi from '@models/TaskJoi'
import { ValidationError } from 'joi'

export class TaskController {
  async get (request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    return await Task.findOne({ id: id }, (err: any, doc: typeof Task) => {
      if (err) {
        console.log(err)
        return response.status(200).json({ success: false, message: "It wasn't possible to get task.", error: err.name })
      } else if (!doc) {
        return response.status(200).json({ success: false, message: 'There is no task with this \'id\'' })
      } else {
        return response.status(200).json({ success: true, task: doc })
      }
    })
  }

  async getAll (request: Request, response: Response): Promise<Response> {
    return await Task.find({}, (err: any, docs: Array<typeof Task>) => {
      if (err) {
        console.log(err)
        return response.status(200).json({ success: false, message: "It wasn't possible to get tasks.", error: err.name })
      } else {
        return response.status(200).json({ success: true, taks: docs })
      }
    })
  }

  async upsertMany (request: Request, response: Response): Promise<Response> {
    const { tasks } = request.body
    const errors: Array<ValidationError> = []
    const upsertedTasks: Array<Object> = []

    if (!tasks || tasks.length === 0) {
      return response.status(200).json({ success: false, error: "Tasks can't be empty." })
    }

    tasks.forEach((task: any) => {
      const { error } = TaskJoi.validate(task)
      if (error) errors.push(error)
    })

    if (errors.length > 0) {
      return response.status(200).json({ success: false, errors })
    }

    try {
      for (const task of tasks) {
        await Task.updateOne({ id: task.id }, task, { new: true, upsert: true }, (err: any, res: any) => {
          if (err) console.log(err)
          if (res.n === 1 || res.nModified === 1) upsertedTasks.push(task)
        })
      }
    } catch (error) {
      console.log(error)
    }

    return response.status(200).json({ success: true, tasks: upsertedTasks })
  }

  async remove (request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    return await Task.findOneAndRemove({ id: id }, null, (err: any, doc: any, res: any) => {
      if (err) {
        console.log(err)
        return response.status(200).json({ success: false, message: "It wasn't possible to delete task.", error: err.name })
      } else if (!doc) {
        return response.status(200).json({ success: false, message: 'There is no task with this \'id\'' })
      } else {
        return response.status(200).json({ success: true, message: 'Task removed with success', task: doc })
      }
    })
  }
}
