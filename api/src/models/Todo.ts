import mongoose from '@config/Database'

const TaskSchema = new mongoose.Schema({
  id: {
    type: String,
    unique: true,
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
  createdAt: {
    type: Date,
    default: Date.now
  }
})

const Task = mongoose.model('Task', TaskSchema)

export default Task
