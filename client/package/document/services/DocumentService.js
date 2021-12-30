export default class DocumentService{
    constructor(options){
        this.documentGateway = options.documentGateway;
    }

    async uploadDocument(file){
        return this.documentGateway.uploadDocument(file);
    }
}