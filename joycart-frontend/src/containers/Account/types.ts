
export type LoginResponseType = {
    status: string;
    data: {
        id: number;
        email: string;
        token: string;
    }
}


export type RegisterResponseType = {
    id: number;
    username: string;
    phoneNumber: string;
    email: string;
    password: string;
}