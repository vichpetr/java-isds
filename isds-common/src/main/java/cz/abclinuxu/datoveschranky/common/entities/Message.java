package cz.abclinuxu.datoveschranky.common.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Celá zpráva, tzn. obálka zprávy + přílohy
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 5L;

    protected MessageEnvelope envelope = null;
    protected TimeStamp timeStamp = null;
    protected Hash hash = null;
    protected List<Attachment> attachments = null;

    public Message() {
    }

    public Message(MessageEnvelope env, List<Attachment> attachs) {
        this.envelope = env;
        this.attachments = attachs;
    }

    public Message(MessageEnvelope envelope, TimeStamp ts, Hash hs, List<Attachment> attachments) {
        this.envelope = envelope;
        this.timeStamp = ts;
        this.hash = hs;
        this.attachments = attachments;
    }

    /**
     * Vrátí seznam příloh.
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * Vrátí obálku zprávy.
     */
    public MessageEnvelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(MessageEnvelope envelope) {
        this.envelope = envelope;
    }

    /**
     * Vrátí časové razítko, je-li obsaženo ve zprávě.
     */
    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("envelope=").append(envelope);
        sb.append(", attachments: ").append(attachments);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        Message message = (Message) o;
        return envelope != null ? envelope.equals(message.envelope) : message.envelope == null;
    }

    @Override
    public int hashCode() {
        return envelope != null ? envelope.hashCode() : 0;
    }
}
