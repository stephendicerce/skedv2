package organizer

class Organization {

    String name
    String description
    Calendar calendar


    static hasMany = [users: User]
    static belongsTo = [orgOwner: User]

    static constraints = {
        description nullable: true
        name nullable: false
        calendar nullable: false
    }
}
