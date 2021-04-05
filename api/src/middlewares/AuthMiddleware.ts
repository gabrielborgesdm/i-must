import { AuthService } from '@services/AuthService'
import { NextFunction, Request, Response } from 'express'

const auth = new AuthService()

export default class AuthMiddleware {
  async authenticateToken (request: Request, response: Response, next: NextFunction) {
    const authHeader = request.headers.authorization
    const token = authHeader && authHeader.split(' ')[1]
    const requestIp: string | null = auth.getIp(request)
    if (!token) return response.status(401).json({ state: 'Unauthorized.' })
    const _id = auth.verifyToken(token, requestIp)
    if (!_id) return response.status(403).json({ state: 'Forbidden.' })
    request.body.user = { _id }
    next()
  }
}
