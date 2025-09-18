
export type LoginResponseType = {
    status:string,
    data: {
        token: string;
        userId: number;
        expiresIn: number;
        refreshToken?: string;
    }
}


export type RegisterResponseType = {
    id: number;
    username: string;
    phoneNumber: string;
    email: string;
    password: string;
}