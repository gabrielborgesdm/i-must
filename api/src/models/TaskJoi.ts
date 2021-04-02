import Joi from 'joi'

export default Joi.object({
  id: Joi.string()
    .required(),
  description: Joi.string()
    .required(),

  completed: Joi.boolean()
    .required()

})
