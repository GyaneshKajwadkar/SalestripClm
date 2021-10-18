
class DocManagerModel
{
    private var name: String? = null
    private var routeName: String? = null
    private var specialityName: String? = null
    private var mailId: String? = null
    private var id: Int? = null
    private var checked: Boolean? = false

    fun getChecked(): Boolean? {
        return checked
    }

    fun setChecked(checked: Boolean?) {
        this.checked = checked
    }

    fun getMailId(): String? {
        return mailId
    }

    fun setMailId(name: String?) {
        this.mailId = name
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getRoute(): String? {
        return routeName
    }

    fun setRoute(routeName: String?) {
        this.routeName = routeName
    }

    fun getSpeciality(): String? {
        return specialityName
    }

    fun setSpeciality(name: String?) {
        this.specialityName = name
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

}