package service

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import dao.UserRepository
import model.User
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait UserService extends IdentityService[User] {
  def save(user: User): Future[WriteResult]
}

class UserServiceImpl @Inject() (userRepository: UserRepository) extends UserService {
  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userRepository.find(loginInfo)

  def save(user: User) = userRepository.save(user)
}
