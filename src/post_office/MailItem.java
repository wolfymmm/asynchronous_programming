package post_office;

// -----------------------------
// Клас листа
// -----------------------------

public class MailItem {
    final String from;
    final String to;

    MailItem(String from, String to) {
        this.from = from;
        this.to = to;
    }
}