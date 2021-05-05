import mongoose from '@config/Database'
import { Schema } from 'mongoose'

const TaskSchema = new mongoose.Schema({
  id: {
    type: String,
    unique: true,
    index: true,
    require: true
  },
  description: {
    type: String,
    require: true
  },
  completed: {
    type: Boolean,
    require: true
  },
  userId: {
    type: Schema.Types.ObjectId,
    ref: 'User',
    require: true
  },
  createdAt: {
    type: Date,
    default: Date.now
  },
  lastUpdated: {
    type: Date
  }
})

const Task = mongoose.model('Task', TaskSchema)

export default Task
