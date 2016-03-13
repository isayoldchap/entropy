package com.sjr.entropy.core.server

import akka.actor.Actor

/**
 * Created by stevenrichardson on 2/6/15.
 */
class UserManagmentActor extends Actor {

  // must have access to all of the user accounts and be able to verify passwords, etc.

  // something else should know who is already signed in and prevent duplicate logins, etc.
  
  override def receive: Receive = {
    case Login(user, password) =>
      // generate a session ID

    case Logout(user) =>
  }
}
