/* eslint-disable import/first */
require('dotenv').config()
import { app } from './app'

if (typeof process.env.DATABASE_CONNECTION_STRING === 'undefined') {
  console.log('Database connection string not found. Make sure you have your .env file set up')
} else {
  app.listen(3333)
}
