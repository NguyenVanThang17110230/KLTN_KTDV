import axios from 'axios';
import getConfig from 'next/config';
import  AccountGateway  from './account/gateways/AccountGateway';
import  AccountService  from './account/services/AccountService';

const restConnector = axios.create({
    baseURL: getConfig().publicRuntimeConfig.BASE_API_URL,
});

const accountGateway = new AccountGateway({restConnector});

export const accountService = new AccountService({accountGateway});