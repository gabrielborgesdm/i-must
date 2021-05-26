import jwt from 'jsonwebtoken'
import bcrypt from 'bcrypt'
import { Request } from 'express'

const accessTokenSecret = String(process.env.ACCESS_TOKEN_SECRET)

export type Payload = {
    ip: string,
    _id: string
};

export class AuthHelpers {
    hashPassword = async (password: string) => await bcrypt.hash(password, 10)

    compareHash = async (userHash: string, accountHash: string) => await bcrypt.compare(userHash, accountHash)

    generateAccessToken = (payload: object) => jwt.sign(payload, accessTokenSecret)

    verifyToken (token: string, requestIp: string): string | null {
      let decoded: Payload
      let _id: string | null = null
      try {
        decoded = jwt.verify(token, accessTokenSecret) as Payload
        if (decoded.ip === requestIp) _id = decoded._id
      } catch (err) {
        console.log(err)
      }
      return _id
    }

    getIp (request: Request): string | null {
      let ip: string | null = null
      if (request.headers['x-forwarded-for']) ip = String(request.headers['x-forwarded-for'])
      else if (request.socket.remoteAddress) ip = request.socket.remoteAddress
      return ip
    }

    verifyEmailToken = (token: string) => jwt.verify(token, accessTokenSecret)
}
