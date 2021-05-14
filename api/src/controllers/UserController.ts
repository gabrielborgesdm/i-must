import User from '@models/User'
import { AuthService } from '@services/AuthService'
import { Request, Response } from 'express'
const auth = new AuthService()

export interface IUser {
  _id: string;
  email: string;
  password: string;
  name: string;
}

export class UserController {
  async signUp (request: Request, response: Response): Promise<Response> {
    const { user } = request.body
    let error = null
    let doc: any
    try {
      const userDoc = await User.findOne({ email: user.email })
      if (userDoc) error = 'user_exists'
      else {
        user.password = await auth.hashPassword(user.password)
        doc = await User.create(user)
      }
    } catch (err) {
      error = 'error'
      console.log(err)
    }

    if (error === 'user_exists') {
      return response.status(200).json({ success: false, status: 'in_use', message: 'E-mail already being used, try with a different one.' })
    } else if (error) {
      return response.status(500).json({ success: false, status: 'server_error', message: 'Something went wrong, try again later.' })
    } else if (doc) {
      return response.status(200).json({ success: true, status: 'operation_executed', message: 'User created with success.' })
    } else {
      return response.status(200).json({ success: false, status: 'error', message: 'It wasn\'t possible to create user.' })
    }
  }

  async signIn (request: Request, response: Response): Promise<Response> {
    const { user } = request.body
    let error = null
    let token: string = null
    try {
      const account: any = await User.findOne({ email: user.email })
      if (account) {
        const isVerified = await auth.compareHash(user.password, account.password)
        token = (isVerified) ? auth.generateAccessToken({ ip: auth.getIp(request), _id: account._id }) : null
      }
    } catch (err) {
      error = true
      console.log(err)
    }

    if (error) {
      return response.status(500).json({ success: false, status: 'server_error', message: 'Something went wrong, try again later.' })
    } else if (token) {
      return response.status(200).json({ success: true, status: 'operation_executed', token, email: user.email })
    } else {
      return response.status(200).json({ success: false, status: 'not_found', message: 'User not found.' })
    }
  }
}
