struct IClusterMessage {
  1: string name;
  2: i64 id;
  3: list<IContainerMessage> containers;
}

struct IContainerMessage {
  1: i64 id;
  2: i64 cluster;
  3: string infoid;
  4: string host;
  5: string image;
  6: string command;
  7: i32 resets;
}

service org.ignis.backend.api.services.IClusterMessageService {
    list<IClusterMessage> sendData();
}
