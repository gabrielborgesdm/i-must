import { NextFunction, Request, Response } from 'express'

export default class ValidationMiddleware {
  validate (schema: any, property: string, propertyType: string = 'body') {
    return (request: Request, response: Response, next: NextFunction) => {
      let propertyValue: any | undefined

      if (propertyType === 'body' && request.body && request.body[property]) {
        propertyValue = request.body[property]
      } else if (propertyType === 'params' && request.params && request.params[property]) {
        propertyValue = request.params[property]
      } else {
        response.status(422).json({ error: `'${property}' is required.` })
        return
      }

      const { error } = schema.validate(propertyValue)
      const valid = error == null
      if (valid) { next() } else {
        response.status(422).json({ error })
      }
    }
  }
}
