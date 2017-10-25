package com.model;

import backtype.storm.tuple.Tuple;
import com.model.serializer.CVParticleSerializer;

import java.util.HashMap;

/**
 * Created by zhoumeng on 2017/10/16
 */
public abstract class CVParticle implements Comparable<CVParticle> {

    private Tuple tuple;
    private long requestId = -1;
    private String streamId;
    private long sequenceNr;
    private HashMap<String, Object> metadata = new HashMap<String, Object>();

    /**
     * Constructs a generic type based on the provided tuple. The tuple must contain streamID and sequenceNR
     * values.
     * @param tuple
     */
    @SuppressWarnings("unchecked")
    public CVParticle(Tuple tuple){
        this(tuple.getStringByField(CVParticleSerializer.STREAMID),
                tuple.getLongByField(CVParticleSerializer.SEQUENCENR));
        this.tuple = tuple;
        this.setRequestId(tuple.getLongByField(CVParticleSerializer.REQUESTID));
        this.setMetadata((HashMap<String, Object>)tuple.getValueByField(CVParticleSerializer.METADATA));
    }

    /**
     * Constructs a GenericType object for some piece of information regarding a stream
     * @param streamId the id of the stream
     * @param sequenceNr the sequence number of the the information (used for ordering)
     */
    public CVParticle(String streamId, long sequenceNr){
        this.streamId = streamId;
        this.sequenceNr = sequenceNr;
    }

    public CVParticle() {}

    public String getStreamId(){
        return streamId;
    }

    public long getSequenceNr(){
        return sequenceNr;
    }

    public Tuple getTuple() {
        return tuple;
    }

    public HashMap<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(HashMap<String, Object> metadata) {
        if(metadata != null){
            this.metadata = metadata;
        }
    }

    public void setRequestId(long id){
        this.requestId = id;
    }

    public long getRequestId() {
        return requestId;
    }

    /**
     * Compares one generictype to another based on their sequence number.
     * @return -1 if this.sequenceNr < other.sequenceNr, 0 if this.sequenceNr == other.sequenceNr else 1
     */
    public int compareTo(CVParticle other){
        return (int)(getSequenceNr() - other.getSequenceNr());
    }


}
