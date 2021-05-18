import Joi from 'joi'

export const TaskSchema = Joi.object({
  id: Joi.string().required(),
  description: Joi.string().required(),
  lastUpdated: Joi.number(),
  datetime: Joi.string(),
  images: Joi.array(),
  completed: Joi.boolean().required()
})

export const TasksSchema = Joi.array().min(1).items(TaskSchema)

export const TaskIdSchema = Joi.string().regex(/^[^\'$&]*$/)
