import mongoose from 'mongoose'

const uri = String(process.env.DATABASE_CONNECTION_STRING)
mongoose.connect(uri, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
  useCreateIndex: true
})
mongoose.Promise = global.Promise

export default mongoose
