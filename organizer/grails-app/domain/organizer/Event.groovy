package organizer

class Event {

    String name
    String description
    int startingMonth
    int startingDay
    int startingYear
    int dueMonth
    int dueDay
    int dueYear
    int dueHour
    int dueMinute
    String color
    int isPublic

    static belongsTo = [user: User, organization: Organization]


    static constraints = {
        name nullable: false
        startingMonth nullable: true
        startingDay nullable: true
        startingYear nullable: true
        description nullable: true
        dueHour nullable: true
        dueMinute nullable: true
        dueDay nullable: false
        dueMonth nullable: false
        dueYear nuallable:false
        color nullable: true
        user nullable: true
        organization nullable: true
    }

}
