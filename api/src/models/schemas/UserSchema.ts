import Joi from 'joi'

export const SignUpSchema = Joi.object({
  name: Joi.string().required(),
  email: Joi.string().email().required(),
  password: Joi.string().required()
})

export const SignInSchema = Joi.object({
  email: Joi.string().email().required(),
  password: Joi.string().required()
})

export const UserSchemaId = Joi.string().regex(/^[^\'$&]*$/)
