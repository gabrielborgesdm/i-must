import { AuthHelpers } from '@helpers/AuthHelpers'
import { NextFunction, Request, Response } from 'express'

const auth = new AuthHelpers()

export default class AuthMiddleware {
  async authenticateToken (request: Request, response: Response, next: NextFunction) {
    const authHeader = request.headers.authorization
    const token = authHeader && authHeader.split(' ')[1]
    const requestIp: string | null = auth.getIp(request)
    if (!token) return response.status(401).json({ success: false, status: 'unauthorized', message: 'Access Unauthorized.' })
    const _id = auth.verifyToken(token, requestIp)
    if (!_id) return response.status(403).json({ success: false, status: 'forbidden', message: 'Access forbidden.' })
    request.body.userId = _id
    next()
  }
}
