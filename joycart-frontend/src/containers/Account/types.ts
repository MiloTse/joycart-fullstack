
export type LoginResponseType = {
    code: number;
    message: string;
    data: {
        id: number;
        token: string;
    }
}


export type RegisterResponseType = {
    code: number;
    message: string;
    data: {
        id: number;
        username: string;
        phoneNumber: string;
        email: string;
        password: string;
    }
}