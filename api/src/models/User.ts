import mongoose from '@config/Database'

const UserSchema = new mongoose.Schema({
  id: {
    type: String,
    unique: true,
    require: true
  },
  name: {
    type: String,
    require: true
  },
  email: {
    type: String,
    require: true
  },
  password: {
    type: String,
    require: true
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
})

const User = mongoose.model('User', UserSchema)

export default User
