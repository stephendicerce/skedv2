
package organizer

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import util.QueryResult

@Transactional
class OrganizationService {
    UserService userService

/**
 *
 * @param token
 * @param description
 * @param name
 * @param result
 * @return
 */
    QueryResult<Organization> createOrganization(AuthToken token, String description, String name, QueryResult<Organization> result = new QueryResult<>(success: true)) {
        User orgOwner = token?.user
        Organization organization = new Organization(name: name, description: description, orgOwner: orgOwner, calendar: new Calendar())
        organization.save(flush: true, failOnError: true)
        result.data = organization

        result
    }

    /**
     * Method to delete the organization.
     * @param token The requesting User's organization.
     * @param organizationId
     * @return
     */
    QueryResult<Organization> deleteOrganization(AuthToken token, String organizationId) {
        QueryResult<Organization> res = new QueryResult<>()
        User requestingUser = token?.user
        long oid = organizationId.isLong() ? organizationId.toLong() : -1

        if(requestingUser != null && oid != -1) {
            Organization organization = Organization.findById(oid)
            if(organization != null) {
                if(isOwnerOf(requestingUser, organization)) {
                    doDelete(organization)
                    res.success = true
                    res
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, res)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
        }
    }

    /**
     *
     * @param organization
     * @param result
     * @return
     */
    private QueryResult<Organization> doDelete(Organization organization) {
        def events = Event.findByOrganization(organization)
        for(event in events) {
            event.delete(flush: true, failOnError: true)
        }
        organization.delete(flush: true, failOnError: true)
    }



    private boolean isOwnerOf(User user, Organization organization) {
        long userId = user.id
        long organizationOwnerId = organization.orgOwner.id

        if(userId == organizationOwnerId)
            true
        else
            false
    }
    /**
     *
     * @param token
     * @return
     */
    QueryResult<List<Organization>> getAllOrganizations(AuthToken token) {
        User requestingUser = token?.user
        QueryResult<List<Organization>> result = new QueryResult<>(success: true)
        if(requestingUser != null) {
            Set<User> userList = new ArrayList<>()
            userList.add(requestingUser)
            result.data = Organization.findAllByOrgOwner(requestingUser)
            result
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
    }

    /**
     *
     * @param token
     * @param organization_id
     * @return
     */
    QueryResult<Organization> getOrganization(AuthToken token, String organization_id) {
        User requestingUser = token?.user
        QueryResult<Organization> result = new QueryResult<>(success: true)
        Long oID = organization_id.isLong() ? organization_id.toLong() : -1

        if (requestingUser != null) {
            if (oID != -1) {
                Set<User> userList = new ArrayList<>()
                userList.add(requestingUser)
                Organization organization = Organization.findById(oID)
                if(isInOrganization(organization, requestingUser))
                    result.data = organization
                else
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
                result
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
    }

    /**
     *
     *
     * @param token
     * @param userId
     * @param orgId
     * @return
     */
    QueryResult<User> addUser(AuthToken token, String userId, String orgId) {
        User requestingUser = token?.user
        QueryResult<User> result = new QueryResult<>()
        Long oID
        Long uID
        if(orgId != null)
            oID = orgId.isLong() ? orgId.toLong() : -1
        else
            oID = -1
        if(userId != null)
            uID = userId.isLong() ? userId.toLong() : -1
        else
            uID = -1

        if(requestingUser != null) {
            Organization organization = Organization.findById(oID)

            if(organization != null) {
                if(isOrganizationOwnerOrAdmin(organization, requestingUser)) {
                    User userToBeAdded = User.findById(uID)
                    if(userToBeAdded != null) {
                        if(!isInOrganization(organization, userToBeAdded)) {
                            organization.addToUsers(userToBeAdded)
                            organization.save(flush:true, failOnError: true)
                            result.data = userToBeAdded
                            result
                        } else {
                            println "1"
                            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                        }
                    } else {
                        println "2"
                        result.message = "User not found"
                        QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                    }
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED,result)
                }
            } else {
                println "3"
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
    }

    /**
     *
     * @param token
     * @param orgId
     * @return
     */
    QueryResult<List<User>> getAllOrganizationUsers(AuthToken token, String orgId) {
        User requestingUser = token?.user
        QueryResult<List<User>> result = new QueryResult<>()
        Long oID
        if(orgId != null) {
            oID = orgId.isLong() ? orgId.toLong() : -1
        } else {
            oID = -1
        }

        if(requestingUser != null) {
            Organization organization = Organization.findById(oID)
            println "ORG ID: "+organization.id
            println "NUMBER OF USERS IN ORGANIZATION: "+organization.users.size()

            if(organization != null) {
                if(isInOrganization(organization, requestingUser)) {
                    //List<User> orgAdmins = organization.admins

                   /* Set<User> applicationUsers = organization.applicationUsers
                    List<User> orgUsers = new ArrayList<>()
                    println organization.orgOwner.firstName
                    User orgOwner = organization.orgOwner
                    println isOrganizationOwner(organization, orgOwner)


                    for(user in applicationUsers) {
                        orgUsers.add(user)
                    }

                    for(user in orgUsers) {
                        result.data.add(user)
                    }
                    */

                    result.data = organization.users
                    result
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
                }
            } else {
                println "4"
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
    }

    /**
     *
     * @param token
     * @param userId
     * @param orgId
     * @param promotion
     * @return
     */
    QueryResult<List<List<User>>> updateUserInOrganization(AuthToken token, String userId, String orgId, String promotionString) {
        User requestingUser = token?.user
        User userToBeUpdated
        QueryResult<List<List<User>>> result= new QueryResult<>(success: true)
        Long oID
        Long uID
        boolean promotion
        if(orgId != null) {
            oID = orgId.isLong() ? orgId.toLong() : -1
        } else {
            oID = -1
        }
        if(userId != null) {
            uID = userId.isLong() ? userId.toLong() : -1
        } else {
            uID = -1
        }

        if(requestingUser != null) {
            userToBeUpdated = User.findById(uID)
            Organization organization = Organization.findById(oID)
            List<User> orgOwnerList = new ArrayList<>()
            User orgOwner = organization.orgOwner
            orgOwnerList.add(orgOwner)
            //List<User> orgAdmins = organization.admins
            List<User> orgUsers = organization.users
            if(isOrganizationOwnerOrAdmin(organization, requestingUser)){
                if(isInOrganization(organization, userToBeUpdated)) {
                    if(!isOrganizationOwner(organization, userToBeUpdated)) {
                        if(isInOrganizationAdmins(organization,userToBeUpdated)) {
                            if(promotionString == null) {
                                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                            } else {
                                if(promotionString.equalsIgnoreCase("true"))
                                    promotion = true
                                else
                                    promotion = false
                                if (!promotion) {
                                   // orgAdmins.remove(userToBeUpdated)
                                    orgUsers.add(userToBeUpdated)
                                } else {
                                    result.message = userToBeUpdated.firstName + " " + userToBeUpdated.lastName + " is already an admin of " + organization.name + "."
                                    QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                                }
                            }
                        } else if(isInOrganizationUsers(organization, userToBeUpdated)) {
                            if(promotion) {
                                orgUsers.remove(userToBeUpdated)
                               // orgAdmins.add(userToBeUpdated)
                            } else {
                                result.message = userToBeUpdated.firstName + " " + userToBeUpdated.lastName  + " is already a user of " + organization.name + "."
                                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                            }
                        } else {
                            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                        }
                        //organization.admins = orgAdmins
                        organization.users = orgUsers
                        organization.save(flush: true, failOnError: true)
                        result.data.add(orgOwnerList)
                        //result.data.add(orgAdmins)
                        result.data.add(orgUsers)
                        result
                    } else {
                        result.message = "Cannot change the status of " + organization.name + "'s Owner."
                        QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
                    }
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
            }

        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }
    }

    /**
     * A method to check if the User an organization's owner or one of the admins
     * @param organization The organization in question
     * @param requestingUser The requesting user
     * @return true if the user is a part of the organization, false if the user is not part of the organization
     */
    boolean isOrganizationOwnerOrAdmin(Organization organization, User requestingUser) {
       // for(User admin: organization.admins) {
         //   if(admin.id == requestingUser.id)
           //     return true
        //}
        if(organization.orgOwner.id == requestingUser.id)
            return true
        return false
    }

    /**
     * A method to check if the User is part of an organization's admin list
     * @param organization The organization in question
     * @param requestingUser The requesting user
     * @return true if the user is a part of the organization, false if the user is not part of the organization
     */
    boolean isInOrganizationAdmins(Organization organization, User requestingUser) {
/*
        for(User admin: organization.admins) {
            if(admin.id == requestingUser.id)
                return true
        }
        */
        return false
    }

    boolean isInOrganizationUsers(Organization organization, User requestingUser) {
        for (User user : organization.users) {
            if (user.id == requestingUser.id)
                return true
        }
        return false
    }

    boolean isOrganizationOwner(Organization organization, User requestingUser) {
        if(organization.orgOwner.id == requestingUser.id)
            return true
        return false
    }

    /**
     * A method to check if the User is part of an organization
     * @param organization The organization in question
     * @param requestingUser The requesting user
     * @return true if the user is a part of the organization, false if the user is not part of the organization
     */
    boolean isInOrganization(Organization organization, User requestingUser) {
        for(User user: organization.users){
            if(user.id == requestingUser.id)
                return true
        }
        /*
        for(User admin: organization.admins) {
            if(admin.id == requestingUser.id)
                return true
        }
        */
        if(organization.orgOwner.id == requestingUser.id)
            return true
        return false
    }
}