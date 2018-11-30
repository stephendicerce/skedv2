package organizer

import util.QueryResult

class OrganizationController {

    static responseFormats = ['json', 'xml']

    PreconditionService preconditionService
    OrganizationService organizationService

    /**
     *
     * @param accessToken
     * @param organization_id
     * @return
     */
    def organizationGet(String accessToken, String organizationId) {
        def require = preconditionService.notNull(params, ["accessToken", "organizationId"])
        def token = preconditionService.accessToken(accessToken,require).data

        if(require.success) {
            QueryResult<Organization> result = organizationService.getOrganization(token, organizationId)
            if(result.success) {
                render(view: 'newOrganization', model: [token: token, organization: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     *
     * @param accessToken
     * @param description
     * @param name
     * @return
     */
    def putOrganization(String accessToken, String description, String name) {
        def require = preconditionService.notNull(params, ["accessToken", "description", "name"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = organizationService.createOrganization(token, description, name)
            if(result.success) {
                render(view: 'newOrganization', model: [token: token, organization: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     *
     * @param accessToken
     * @param organization_id
     * @return
     */
    def deleteOrganization(String accessToken, String organizationId) {
        def require = preconditionService.notNull(params, ["accessToken", "organizationId"])
        def token = preconditionService.accessToken(accessToken,require).data

        if(require.success) {
            def result = organizationService.deleteOrganization(token, organizationId)
            if(result.success) {
                render(view: 'deleteResult', model: [token: token])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     *
     * @param accessToken
     * @param userId
     * @param orgId
     * @return
     */
    def postUser(String accessToken, String userId, String orgId) {
        def require = preconditionService.notNull(params, ["accessToken", "userId", "orgId"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = organizationService.addUser(token, userId, orgId)

            if(result.success) {
                render(view: 'orgUser', model: [token: token, user: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     *
     * @param accessToken
     * @return
     */
    def getAllOrganizations(String accessToken) {
        def require = preconditionService.notNull(params, ["accessToken"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = organizationService.getAllOrganizations(token)

            if(result.success) {
                render(view: 'organizationList', model: [token: token, organizations: result.data])
            } else {
                render('../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     *
     * @param accessToken
     * @param orgId
     * @return
     */
    def getOrganizationUsers(String accessToken, String orgId) {
        def require = preconditionService.notNull(params, ["accessToken", "orgId"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = organizationService.getAllOrganizationUsers(token, orgId)

            if(result.success) {
                println "USER INFO: user Id:" + result.data[0].id + "\n        user Name: " + result.data[0].firstName + " " + result.data[0].lastName + "\n        user Email: " + result.data[0].email
                render(view: 'userList', model: [token: token, users: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     *
     * @param accessToken
     * @param userId
     * @param orgId
     * @param promotionString
     * @return
     */
    def updateUser(String accessToken, String userId, String orgId, String promotionString) {
        def require = preconditionService.notNull(params, ["accessToken", "userId", "orgId", "promotionString"])
        def token = preconditionService.accessToken(accessToken, require).data

        if(require.success) {
            def result = organizationService.updateUserInOrganization(token, userId, orgId, promotionString)

            if(result.success) {
                render(view: 'usersList', model: [token: token, orgUsers: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

}
