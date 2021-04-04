import { NextFunction, Request, Response } from 'express'

export default class ValidationMiddleware {
    validate(schema: any, property: string, propertyType: string = 'body') {
        return (request: Request, response: Response, next: NextFunction) => {
            let propertyValue = (propertyType === 'body') ? request.body[property] : request.params[property]
            const { error } = schema.validate(propertyValue)
            const valid = error == null
            if (valid) { next() }
            else {
                response.status(422).json({ error })
            }
        }
    }
}