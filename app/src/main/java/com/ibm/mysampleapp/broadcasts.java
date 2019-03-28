
package com.ibm.mysampleapp;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class broadcasts
{
  private String ownerId;
  private String vodId;
  private Integer duration;
  private String streamId;
  private java.util.Date created;
  private String objectId;
  private String type;
  private Integer fileSize;
  private String streamName;
  private java.util.Date updated;
  private Integer creationDate;
  private String filePath;
  private String vodName;
  public String getOwnerId()
  {
    return ownerId;
  }

  public String getVodId()
  {
    return vodId;
  }

  public void setVodId( String vodId )
  {
    this.vodId = vodId;
  }

  public Integer getDuration()
  {
    return duration;
  }

  public void setDuration( Integer duration )
  {
    this.duration = duration;
  }

  public String getStreamId()
  {
    return streamId;
  }

  public void setStreamId( String streamId )
  {
    this.streamId = streamId;
  }

  public java.util.Date getCreated()
  {
    return created;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public Integer getFileSize()
  {
    return fileSize;
  }

  public void setFileSize( Integer fileSize )
  {
    this.fileSize = fileSize;
  }

  public String getStreamName()
  {
    return streamName;
  }

  public void setStreamName( String streamName )
  {
    this.streamName = streamName;
  }

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public Integer getCreationDate()
  {
    return creationDate;
  }

  public void setCreationDate( Integer creationDate )
  {
    this.creationDate = creationDate;
  }

  public String getFilePath()
  {
    return filePath;
  }

  public void setFilePath( String filePath )
  {
    this.filePath = filePath;
  }

  public String getVodName()
  {
    return vodName;
  }

  public void setVodName( String vodName )
  {
    this.vodName = vodName;
  }

                                                    
  public broadcasts save()
  {
    return Backendless.Data.of( broadcasts.class ).save( this );
  }

  public void saveAsync( AsyncCallback<broadcasts> callback )
  {
    Backendless.Data.of( broadcasts.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( broadcasts.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( broadcasts.class ).remove( this, callback );
  }

  public static broadcasts findById( String id )
  {
    return Backendless.Data.of( broadcasts.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<broadcasts> callback )
  {
    Backendless.Data.of( broadcasts.class ).findById( id, callback );
  }

  public static broadcasts findFirst()
  {
    return Backendless.Data.of( broadcasts.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<broadcasts> callback )
  {
    Backendless.Data.of( broadcasts.class ).findFirst( callback );
  }

  public static broadcasts findLast()
  {
    return Backendless.Data.of( broadcasts.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<broadcasts> callback )
  {
    Backendless.Data.of( broadcasts.class ).findLast( callback );
  }

  public static List<broadcasts> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( broadcasts.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<broadcasts>> callback )
  {
    Backendless.Data.of( broadcasts.class ).find( queryBuilder, callback );
  }
}