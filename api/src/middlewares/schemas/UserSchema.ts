import Joi from 'joi'

export const UserSchema = Joi.object({
    id: Joi.string().required(),
    name: Joi.string().required(),
    email: Joi.string().email().required(),
    password: Joi.string().required()
})

export const UserSchemaId = Joi.string().regex(/^[^\'$&]*$/)
