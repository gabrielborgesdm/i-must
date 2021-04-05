import { Request, Response } from 'express'

import Task from '@models/Task'

export class TaskController {
  async get (request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    let doc: any = null
    let error = false

    try {
      doc = await Task.findOne({ $and: [{ id }, { userId: request.body.userId }] })
    } catch (err) {
      console.log(err)
      error = true
    }

    if (error) {
      return response.status(200).json({ success: false, status: 'server_error', message: 'It wasn\'t possible to get task.' })
    } else if (!doc) {
      return response.status(200).json({ success: false, status: 'not_found', message: 'There is no task with this \'id\'.' })
    } else {
      return response.status(200).json({ success: true, status: 'operation_executed', task: doc })
    }
  }

  async getAll (request: Request, response: Response): Promise<Response> {
    let docs: any = null
    let error = false

    try {
      docs = await Task.find({ userId: request.body.userId })
    } catch (err) {
      console.log(err)
      error = true
    }
    if (error) {
      console.log(error)
      return response.status(200).json({ success: false, status: 'server_error', message: "It wasn't possible to get tasks." })
    } else {
      return response.status(200).json({ success: true, status: 'operation_executed', taks: docs })
    }
  }

  async upsertMany (request: Request, response: Response): Promise<Response> {
    const { tasks, userId } = request.body
    const upsertedTasks: Array<Object> = []
    let error = false

    for (const task of tasks) {
      try {
        task.userId = userId
        const res = await Task.updateOne({ $and: [{ id: task.id }, { userId }] }, task, { new: true, upsert: true })
        if (res.ok) upsertedTasks.push(task)
      } catch (err) {
        error = true
        console.log(err)
      }
    }
    if (error && upsertedTasks.length === 0) {
      return response.status(200).json({ success: false, status: 'server_error', message: 'Something went wrong, try again later.' })
    } else {
      return response.status(200).json({ success: true, status: 'operation_executed', tasks: upsertedTasks })
    }
  }

  async delete (request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    let doc: any = null
    let error = false

    try {
      doc = await Task.findOneAndRemove({ $and: [{ id }, { userId: request.body.userId }] })
    } catch (err) {
      console.log(err)
      error = true
    }

    if (error) {
      return response.status(200).json({ success: false, status: 'server_error', message: 'It wasn\'t possible to delete task.' })
    } else if (!doc) {
      return response.status(200).json({ success: false, status: 'not_found', message: 'There is no task with this \'id\'.' })
    } else {
      return response.status(200).json({ success: true, status: 'operation_executed', task: doc })
    }
  }
}
