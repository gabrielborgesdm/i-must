import { Request, Response } from 'express'

import Task from '@models/Task'
import { ValidationError } from 'joi'

export class TaskController {
  async get(request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    return await Task.findOne({ id: id }, (err: any, doc: typeof Task) => {
      if (err) {
        console.log(err)
        return response.status(200).json({ success: false, message: "It wasn't possible to get task.", error: err.name })
      } else if (!doc) {
        return response.status(200).json({ success: false, message: 'There is no task with this \'id\'.' })
      } else {
        return response.status(200).json({ success: true, task: doc })
      }
    })
  }

  async getAll(request: Request, response: Response): Promise<Response> {
    return await Task.find({}, (err: any, docs: Array<typeof Task>) => {
      if (err) {
        console.log(err)
        return response.status(200).json({ success: false, message: "It wasn't possible to get tasks.", error: err.name })
      } else {
        return response.status(200).json({ success: true, taks: docs })
      }
    })
  }

  async upsertMany(request: Request, response: Response): Promise<Response> {
    const { tasks } = request.body
    const upsertedTasks: Array<Object> = []
    let error = false

    for (const task of tasks) {
      try {
        let res = await Task.updateOne({ id: task.id }, task, { new: true, upsert: true })
        if (res.ok) upsertedTasks.push(task)
      } catch (err) {
        error = true
        console.log(err)
      }
    }
    if (error && upsertedTasks.length === 0) {
      return response.status(200).json({ success: false, message: 'Something went wrong, try again later.' })
    } else {
      return response.status(200).json({ success: true, tasks: upsertedTasks })
    }
  }

  async remove(request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    let error = false
    let doc = null
    try {
      doc = await Task.findOneAndRemove({ id: id })
    } catch (err) {
      console.log(err)
      error = true
    }
    if (error) {
      return response.status(200).json({ success: false, message: 'Something went wrong, try again later.' })
    } else if (!doc) {
      return response.status(200).json({ success: false, message: 'There is no task with this \'id\'.' })
    } else {
      return response.status(200).json({ success: true, message: 'Task removed with success.', task: doc })
    }
  }
}
