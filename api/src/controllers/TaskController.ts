import { Request, Response } from 'express'

import Task from '@models/Task'
import { ImageHelpers } from '@helpers/ImageHelpers'

export class TaskController {
  ImageHelpers: ImageHelpers

  constructor () {
    this.ImageHelpers = new ImageHelpers()
  }

  async get (request: Request, response: Response): Promise<Response> {
    const id = request.params.id
    let doc: any = null
    let error = false

    try {
      doc = await Task.findOne({ $and: [{ id }, { userId: request.body.userId }] })
      doc = this.filterTaskDoc(doc._doc)
    } catch (err) {
      console.log(err)
      error = true
    }

    if (error) {
      return response.status(500).json({ success: false, status: 'server_error', message: 'It wasn\'t possible to get task.' })
    } else if (!doc) {
      return response.status(200).json({ success: false, status: 'not_found', message: 'There is no task with this \'id\'.' })
    } else {
      return response.status(200).json({ success: true, status: 'operation_executed', task: doc })
    }
  }

  filterTaskDoc (task: any): any {
    const newDoc = { ...task }
    newDoc.lastUpdated = new Date(task.lastUpdated).getTime()
    if (newDoc.createdAt) {
      newDoc.createdAt = new Date(task.createdAt).getTime()
    }
    if (task.imagePaths && task.imagePaths.length) {
      const images = this.ImageHelpers.getImages(task.imagePaths)
      newDoc.images = images
      delete newDoc.imagePaths
    }
    delete newDoc.__v
    delete newDoc._id
    delete newDoc.$setOnInsert
    return newDoc
  }

  async getAll (request: Request, response: Response): Promise<Response> {
    let docs: any = null
    let error = false

    try {
      docs = await Task.find({ userId: request.body.userId })
      docs = docs.map((doc: any) => this.filterTaskDoc(doc._doc))
    } catch (err) {
      console.log(err)
      error = true
    }
    if (error) {
      console.log(error)
      return response.status(500).json({ success: false, status: 'server_error', message: "It wasn't possible to get tasks." })
    } else {
      return response.status(200).json({ success: true, status: 'operation_executed', tasks: docs })
    }
  }

  async upsertMany (request: Request, response: Response): Promise<Response> {
    const { tasks, userId } = request.body
    const upsertedTasks: Array<Object> = []

    let error = false

    for (const task of tasks) {
      try {
        task.userId = userId
        if (task.images) {
          task.imagePaths = this.ImageHelpers.writeImages(task.userId, task.images)
          delete task.images
        }
        const oldTask: any = await Task.findOne({ $and: [{ id: task.id }, { userId }] })
        const res = await Task.updateOne({ $and: [{ id: task.id }, { userId }] }, task, { new: true, upsert: true })
        if (res.ok) {
          if (oldTask && oldTask.imagePaths) {
            this.ImageHelpers.removeImages(oldTask.imagePaths)
          }
          upsertedTasks.push(this.filterTaskDoc(task))
        }
      } catch (err) {
        error = true
        console.log(err)
      }
    }
    if (error && upsertedTasks.length === 0) {
      return response.status(500).json({ success: false, status: 'server_error', message: 'Something went wrong, try again later.' })
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
      if (doc && doc._doc) {
        doc = this.filterTaskDoc(doc._doc)
        if (doc && doc.imagePaths) {
          this.ImageHelpers.removeImages(doc.imagePaths)
        }
      }
    } catch (err) {
      console.log(err)
      error = true
    }

    if (error) {
      return response.status(500).json({ success: false, status: 'server_error', message: 'It wasn\'t possible to delete task.' })
    } else if (!doc) {
      return response.status(200).json({ success: false, status: 'not_found', message: 'There is no task with this \'id\'.' })
    } else {
      return response.status(200).json({ success: true, status: 'operation_executed', task: doc })
    }
  }
}
