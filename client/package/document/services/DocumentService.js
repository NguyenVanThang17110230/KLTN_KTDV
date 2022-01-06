export default class DocumentService{
    constructor(options){
        this.documentGateway = options.documentGateway;
    }

    async uploadDocument(file){
        return this.documentGateway.uploadDocument(file);
    }
    
    async getDocument(){
        return this.documentGateway.getDocument();
    }

    async deleteDocument(id){
        return this.documentGateway.deleteDocument(id);
    }

    async updateDocument(id,value){
        return this.documentGateway.updateDocument(id,value);
    }

    async getListDocumentByAdmin(){
        return this.documentGateway.getListDocumentByAdmin();
    }

    async getDetailDocument(id){
        return this.documentGateway.getDetailDocument(id)
    }

}