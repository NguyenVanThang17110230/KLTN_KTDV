export default class DocumentGateway {
  constructor(options) {
    this.restConnector = options.restConnector;
  }

  async uploadDocument(file) {
    const resp = await this.restConnector.post("/document/upload", file);
    return resp.data;
  }
  
  async deleteDocument(id) {
    const resp = await this.restConnector.delete("/document/"+id);
    return resp.data;
  }

  async updateDocument(id,value) {
    const resp = await this.restConnector.patch("/document/"+id, value);
    return resp.data;
  }
  
  async getDocument(){
    const resp = await this.restConnector.get("/document/documents");
    return resp.data;
  }

  async getListDocumentByAdmin(){
    const resp = await this.restConnector.get("/document/manager");
    return resp.data;
  }

  async getDetailDocument(id){
    const resp = await this.restConnector.get("/document/details/"+id);
    return resp.data;
  }

  async getDocumentTest(){
    const resp = await this.restConnector.get("/document/ftp");
    return resp.data;
  }
}
