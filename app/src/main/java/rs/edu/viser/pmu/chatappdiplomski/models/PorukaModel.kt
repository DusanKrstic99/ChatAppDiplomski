package rs.edu.viser.pmu.chatappdiplomski.models

class PorukaModel(val id: String, val text: String, val odId: String, val zaId: String, val timestamp: Long) {
    constructor() : this("", "", "", "", -1)
}
