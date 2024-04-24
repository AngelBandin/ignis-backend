/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2024-04-12")
public class IContainerMessage implements org.apache.thrift.TBase<IContainerMessage, IContainerMessage._Fields>, java.io.Serializable, Cloneable, Comparable<IContainerMessage> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("IContainerMessage");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField CLUSTER_FIELD_DESC = new org.apache.thrift.protocol.TField("cluster", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField INFOID_FIELD_DESC = new org.apache.thrift.protocol.TField("infoid", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField HOST_FIELD_DESC = new org.apache.thrift.protocol.TField("host", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField IMAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("image", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField COMMAND_FIELD_DESC = new org.apache.thrift.protocol.TField("command", org.apache.thrift.protocol.TType.STRING, (short)6);
  private static final org.apache.thrift.protocol.TField RESETS_FIELD_DESC = new org.apache.thrift.protocol.TField("resets", org.apache.thrift.protocol.TType.I32, (short)7);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new IContainerMessageStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new IContainerMessageTupleSchemeFactory();

  public long id; // required
  public long cluster; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String infoid; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String host; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String image; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String command; // required
  public int resets; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    CLUSTER((short)2, "cluster"),
    INFOID((short)3, "infoid"),
    HOST((short)4, "host"),
    IMAGE((short)5, "image"),
    COMMAND((short)6, "command"),
    RESETS((short)7, "resets");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ID
          return ID;
        case 2: // CLUSTER
          return CLUSTER;
        case 3: // INFOID
          return INFOID;
        case 4: // HOST
          return HOST;
        case 5: // IMAGE
          return IMAGE;
        case 6: // COMMAND
          return COMMAND;
        case 7: // RESETS
          return RESETS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    @Override
    public short getThriftFieldId() {
      return _thriftId;
    }

    @Override
    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ID_ISSET_ID = 0;
  private static final int __CLUSTER_ISSET_ID = 1;
  private static final int __RESETS_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.CLUSTER, new org.apache.thrift.meta_data.FieldMetaData("cluster", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.INFOID, new org.apache.thrift.meta_data.FieldMetaData("infoid", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.HOST, new org.apache.thrift.meta_data.FieldMetaData("host", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.IMAGE, new org.apache.thrift.meta_data.FieldMetaData("image", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.COMMAND, new org.apache.thrift.meta_data.FieldMetaData("command", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.RESETS, new org.apache.thrift.meta_data.FieldMetaData("resets", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(IContainerMessage.class, metaDataMap);
  }

  public IContainerMessage() {
  }

  public IContainerMessage(
    long id,
    long cluster,
    java.lang.String infoid,
    java.lang.String host,
    java.lang.String image,
    java.lang.String command,
    int resets)
  {
    this();
    this.id = id;
    setIdIsSet(true);
    this.cluster = cluster;
    setClusterIsSet(true);
    this.infoid = infoid;
    this.host = host;
    this.image = image;
    this.command = command;
    this.resets = resets;
    setResetsIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public IContainerMessage(IContainerMessage other) {
    __isset_bitfield = other.__isset_bitfield;
    this.id = other.id;
    this.cluster = other.cluster;
    if (other.isSetInfoid()) {
      this.infoid = other.infoid;
    }
    if (other.isSetHost()) {
      this.host = other.host;
    }
    if (other.isSetImage()) {
      this.image = other.image;
    }
    if (other.isSetCommand()) {
      this.command = other.command;
    }
    this.resets = other.resets;
  }

  @Override
  public IContainerMessage deepCopy() {
    return new IContainerMessage(this);
  }

  @Override
  public void clear() {
    setIdIsSet(false);
    this.id = 0;
    setClusterIsSet(false);
    this.cluster = 0;
    this.infoid = null;
    this.host = null;
    this.image = null;
    this.command = null;
    setResetsIsSet(false);
    this.resets = 0;
  }

  public long getId() {
    return this.id;
  }

  public IContainerMessage setId(long id) {
    this.id = id;
    setIdIsSet(true);
    return this;
  }

  public void unsetId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ID_ISSET_ID);
  }

  /** Returns true if field id is set (has been assigned a value) and false otherwise */
  public boolean isSetId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ID_ISSET_ID);
  }

  public void setIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ID_ISSET_ID, value);
  }

  public long getCluster() {
    return this.cluster;
  }

  public IContainerMessage setCluster(long cluster) {
    this.cluster = cluster;
    setClusterIsSet(true);
    return this;
  }

  public void unsetCluster() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __CLUSTER_ISSET_ID);
  }

  /** Returns true if field cluster is set (has been assigned a value) and false otherwise */
  public boolean isSetCluster() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __CLUSTER_ISSET_ID);
  }

  public void setClusterIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __CLUSTER_ISSET_ID, value);
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getInfoid() {
    return this.infoid;
  }

  public IContainerMessage setInfoid(@org.apache.thrift.annotation.Nullable java.lang.String infoid) {
    this.infoid = infoid;
    return this;
  }

  public void unsetInfoid() {
    this.infoid = null;
  }

  /** Returns true if field infoid is set (has been assigned a value) and false otherwise */
  public boolean isSetInfoid() {
    return this.infoid != null;
  }

  public void setInfoidIsSet(boolean value) {
    if (!value) {
      this.infoid = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getHost() {
    return this.host;
  }

  public IContainerMessage setHost(@org.apache.thrift.annotation.Nullable java.lang.String host) {
    this.host = host;
    return this;
  }

  public void unsetHost() {
    this.host = null;
  }

  /** Returns true if field host is set (has been assigned a value) and false otherwise */
  public boolean isSetHost() {
    return this.host != null;
  }

  public void setHostIsSet(boolean value) {
    if (!value) {
      this.host = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getImage() {
    return this.image;
  }

  public IContainerMessage setImage(@org.apache.thrift.annotation.Nullable java.lang.String image) {
    this.image = image;
    return this;
  }

  public void unsetImage() {
    this.image = null;
  }

  /** Returns true if field image is set (has been assigned a value) and false otherwise */
  public boolean isSetImage() {
    return this.image != null;
  }

  public void setImageIsSet(boolean value) {
    if (!value) {
      this.image = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getCommand() {
    return this.command;
  }

  public IContainerMessage setCommand(@org.apache.thrift.annotation.Nullable java.lang.String command) {
    this.command = command;
    return this;
  }

  public void unsetCommand() {
    this.command = null;
  }

  /** Returns true if field command is set (has been assigned a value) and false otherwise */
  public boolean isSetCommand() {
    return this.command != null;
  }

  public void setCommandIsSet(boolean value) {
    if (!value) {
      this.command = null;
    }
  }

  public int getResets() {
    return this.resets;
  }

  public IContainerMessage setResets(int resets) {
    this.resets = resets;
    setResetsIsSet(true);
    return this;
  }

  public void unsetResets() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __RESETS_ISSET_ID);
  }

  /** Returns true if field resets is set (has been assigned a value) and false otherwise */
  public boolean isSetResets() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __RESETS_ISSET_ID);
  }

  public void setResetsIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __RESETS_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((java.lang.Long)value);
      }
      break;

    case CLUSTER:
      if (value == null) {
        unsetCluster();
      } else {
        setCluster((java.lang.Long)value);
      }
      break;

    case INFOID:
      if (value == null) {
        unsetInfoid();
      } else {
        setInfoid((java.lang.String)value);
      }
      break;

    case HOST:
      if (value == null) {
        unsetHost();
      } else {
        setHost((java.lang.String)value);
      }
      break;

    case IMAGE:
      if (value == null) {
        unsetImage();
      } else {
        setImage((java.lang.String)value);
      }
      break;

    case COMMAND:
      if (value == null) {
        unsetCommand();
      } else {
        setCommand((java.lang.String)value);
      }
      break;

    case RESETS:
      if (value == null) {
        unsetResets();
      } else {
        setResets((java.lang.Integer)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return getId();

    case CLUSTER:
      return getCluster();

    case INFOID:
      return getInfoid();

    case HOST:
      return getHost();

    case IMAGE:
      return getImage();

    case COMMAND:
      return getCommand();

    case RESETS:
      return getResets();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  @Override
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case ID:
      return isSetId();
    case CLUSTER:
      return isSetCluster();
    case INFOID:
      return isSetInfoid();
    case HOST:
      return isSetHost();
    case IMAGE:
      return isSetImage();
    case COMMAND:
      return isSetCommand();
    case RESETS:
      return isSetResets();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof IContainerMessage)
      return this.equals((IContainerMessage)that);
    return false;
  }

  public boolean equals(IContainerMessage that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_id = true;
    boolean that_present_id = true;
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (this.id != that.id)
        return false;
    }

    boolean this_present_cluster = true;
    boolean that_present_cluster = true;
    if (this_present_cluster || that_present_cluster) {
      if (!(this_present_cluster && that_present_cluster))
        return false;
      if (this.cluster != that.cluster)
        return false;
    }

    boolean this_present_infoid = true && this.isSetInfoid();
    boolean that_present_infoid = true && that.isSetInfoid();
    if (this_present_infoid || that_present_infoid) {
      if (!(this_present_infoid && that_present_infoid))
        return false;
      if (!this.infoid.equals(that.infoid))
        return false;
    }

    boolean this_present_host = true && this.isSetHost();
    boolean that_present_host = true && that.isSetHost();
    if (this_present_host || that_present_host) {
      if (!(this_present_host && that_present_host))
        return false;
      if (!this.host.equals(that.host))
        return false;
    }

    boolean this_present_image = true && this.isSetImage();
    boolean that_present_image = true && that.isSetImage();
    if (this_present_image || that_present_image) {
      if (!(this_present_image && that_present_image))
        return false;
      if (!this.image.equals(that.image))
        return false;
    }

    boolean this_present_command = true && this.isSetCommand();
    boolean that_present_command = true && that.isSetCommand();
    if (this_present_command || that_present_command) {
      if (!(this_present_command && that_present_command))
        return false;
      if (!this.command.equals(that.command))
        return false;
    }

    boolean this_present_resets = true;
    boolean that_present_resets = true;
    if (this_present_resets || that_present_resets) {
      if (!(this_present_resets && that_present_resets))
        return false;
      if (this.resets != that.resets)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(id);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(cluster);

    hashCode = hashCode * 8191 + ((isSetInfoid()) ? 131071 : 524287);
    if (isSetInfoid())
      hashCode = hashCode * 8191 + infoid.hashCode();

    hashCode = hashCode * 8191 + ((isSetHost()) ? 131071 : 524287);
    if (isSetHost())
      hashCode = hashCode * 8191 + host.hashCode();

    hashCode = hashCode * 8191 + ((isSetImage()) ? 131071 : 524287);
    if (isSetImage())
      hashCode = hashCode * 8191 + image.hashCode();

    hashCode = hashCode * 8191 + ((isSetCommand()) ? 131071 : 524287);
    if (isSetCommand())
      hashCode = hashCode * 8191 + command.hashCode();

    hashCode = hashCode * 8191 + resets;

    return hashCode;
  }

  @Override
  public int compareTo(IContainerMessage other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetId(), other.isSetId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, other.id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetCluster(), other.isSetCluster());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCluster()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cluster, other.cluster);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetInfoid(), other.isSetInfoid());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetInfoid()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.infoid, other.infoid);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetHost(), other.isSetHost());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHost()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.host, other.host);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetImage(), other.isSetImage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetImage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.image, other.image);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetCommand(), other.isSetCommand());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCommand()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.command, other.command);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetResets(), other.isSetResets());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResets()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resets, other.resets);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  @Override
  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  @Override
  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("IContainerMessage(");
    boolean first = true;

    sb.append("id:");
    sb.append(this.id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("cluster:");
    sb.append(this.cluster);
    first = false;
    if (!first) sb.append(", ");
    sb.append("infoid:");
    if (this.infoid == null) {
      sb.append("null");
    } else {
      sb.append(this.infoid);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("host:");
    if (this.host == null) {
      sb.append("null");
    } else {
      sb.append(this.host);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("image:");
    if (this.image == null) {
      sb.append("null");
    } else {
      sb.append(this.image);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("command:");
    if (this.command == null) {
      sb.append("null");
    } else {
      sb.append(this.command);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("resets:");
    sb.append(this.resets);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class IContainerMessageStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public IContainerMessageStandardScheme getScheme() {
      return new IContainerMessageStandardScheme();
    }
  }

  private static class IContainerMessageStandardScheme extends org.apache.thrift.scheme.StandardScheme<IContainerMessage> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, IContainerMessage struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.id = iprot.readI64();
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CLUSTER
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.cluster = iprot.readI64();
              struct.setClusterIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // INFOID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.infoid = iprot.readString();
              struct.setInfoidIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // HOST
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.host = iprot.readString();
              struct.setHostIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // IMAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.image = iprot.readString();
              struct.setImageIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // COMMAND
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.command = iprot.readString();
              struct.setCommandIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // RESETS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.resets = iprot.readI32();
              struct.setResetsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, IContainerMessage struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ID_FIELD_DESC);
      oprot.writeI64(struct.id);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(CLUSTER_FIELD_DESC);
      oprot.writeI64(struct.cluster);
      oprot.writeFieldEnd();
      if (struct.infoid != null) {
        oprot.writeFieldBegin(INFOID_FIELD_DESC);
        oprot.writeString(struct.infoid);
        oprot.writeFieldEnd();
      }
      if (struct.host != null) {
        oprot.writeFieldBegin(HOST_FIELD_DESC);
        oprot.writeString(struct.host);
        oprot.writeFieldEnd();
      }
      if (struct.image != null) {
        oprot.writeFieldBegin(IMAGE_FIELD_DESC);
        oprot.writeString(struct.image);
        oprot.writeFieldEnd();
      }
      if (struct.command != null) {
        oprot.writeFieldBegin(COMMAND_FIELD_DESC);
        oprot.writeString(struct.command);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(RESETS_FIELD_DESC);
      oprot.writeI32(struct.resets);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class IContainerMessageTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public IContainerMessageTupleScheme getScheme() {
      return new IContainerMessageTupleScheme();
    }
  }

  private static class IContainerMessageTupleScheme extends org.apache.thrift.scheme.TupleScheme<IContainerMessage> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, IContainerMessage struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetId()) {
        optionals.set(0);
      }
      if (struct.isSetCluster()) {
        optionals.set(1);
      }
      if (struct.isSetInfoid()) {
        optionals.set(2);
      }
      if (struct.isSetHost()) {
        optionals.set(3);
      }
      if (struct.isSetImage()) {
        optionals.set(4);
      }
      if (struct.isSetCommand()) {
        optionals.set(5);
      }
      if (struct.isSetResets()) {
        optionals.set(6);
      }
      oprot.writeBitSet(optionals, 7);
      if (struct.isSetId()) {
        oprot.writeI64(struct.id);
      }
      if (struct.isSetCluster()) {
        oprot.writeI64(struct.cluster);
      }
      if (struct.isSetInfoid()) {
        oprot.writeString(struct.infoid);
      }
      if (struct.isSetHost()) {
        oprot.writeString(struct.host);
      }
      if (struct.isSetImage()) {
        oprot.writeString(struct.image);
      }
      if (struct.isSetCommand()) {
        oprot.writeString(struct.command);
      }
      if (struct.isSetResets()) {
        oprot.writeI32(struct.resets);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, IContainerMessage struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(7);
      if (incoming.get(0)) {
        struct.id = iprot.readI64();
        struct.setIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.cluster = iprot.readI64();
        struct.setClusterIsSet(true);
      }
      if (incoming.get(2)) {
        struct.infoid = iprot.readString();
        struct.setInfoidIsSet(true);
      }
      if (incoming.get(3)) {
        struct.host = iprot.readString();
        struct.setHostIsSet(true);
      }
      if (incoming.get(4)) {
        struct.image = iprot.readString();
        struct.setImageIsSet(true);
      }
      if (incoming.get(5)) {
        struct.command = iprot.readString();
        struct.setCommandIsSet(true);
      }
      if (incoming.get(6)) {
        struct.resets = iprot.readI32();
        struct.setResetsIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
