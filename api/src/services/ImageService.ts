import fs from 'fs'
import path from 'path'
import crypto from 'crypto'

export class ImageService {
    IMAGE_PATH: string;
    IMAGE_EXTENSION: string;

    constructor () {
      this.IMAGE_PATH = path.resolve(`${__dirname}../../../storage/images`)
      this.IMAGE_EXTENSION = '.jpg'
    }

    public getImageName (userId: string, uniqueImageId: number) {
      const timestamp = new Date().getTime()
      return `${this.hashCode(userId)}${uniqueImageId}${timestamp}${this.IMAGE_EXTENSION}`
    }

    private hashCode (code: string) {
      return crypto.createHash('md5').update(code).digest('hex')
    }

    private removeBase64Header (base64: string) {
      const base64Array = base64.split(',')
      return base64Array[base64Array.length - 1]
    }

    private writeImage (userId: string, uniqueImageId: number, base64: string) {
      const imageName = this.getImageName(userId, uniqueImageId)
      base64 = this.removeBase64Header(base64)
      try {
        fs.writeFileSync(`${this.IMAGE_PATH}/${imageName}`, base64, { encoding: 'base64' })
      } catch (error) {
        console.log(error)
        return false
      }
      return imageName
    }

    public writeImages (userId: string, images: Array<string>) {
      const paths = []
      let index = 0
      if (fs.existsSync(this.IMAGE_PATH)) {
        for (const base64 of images) {
          paths.push(this.writeImage(userId, index, base64))
          index++
        }
      }
      return paths
    }

    private removeImage (imageRelativePath: string) {
      const fullImagePath = `${this.IMAGE_PATH}/${imageRelativePath}`
      if (!fs.existsSync(fullImagePath)) return null
      try {
        fs.unlinkSync(fullImagePath)
      } catch (error) {
        console.log(error)
        return null
      }
      return imageRelativePath
    }

    public removeImages (imagePaths: Array<string>) {
      const removedPaths = []
      if (fs.existsSync(this.IMAGE_PATH)) {
        for (const path of imagePaths) {
          const removedImage = this.removeImage(path)
          if (removedImage) {
            removedPaths.push(removedImage)
          }
        }
      }

      return removedPaths
    }

    private convertImageToBase64 (imageRelativePath: string) {
      let base64Image = null
      const fullImagePath = `${this.IMAGE_PATH}/${imageRelativePath}`
      if (fs.existsSync(fullImagePath)) {
        try {
          const file = fs.readFileSync(fullImagePath)
          base64Image = file.toString('base64')
        } catch (error) {
          console.log(error)
        }
      }
      return base64Image
    }

    public getImages (imagePaths: Array<string>) {
      const images = []
      for (const imagePath of imagePaths) {
        const base64Image = this.convertImageToBase64(imagePath)
        if (base64Image) images.push(base64Image)
      }
      return images
    }
}
