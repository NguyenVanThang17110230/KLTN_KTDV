import Head from "next/head";
import Image from "next/image";
import { FC, ReactElement, useEffect } from "react";
import UserGuest from "../layouts/UserGuest";
import { Formik, Field, Form, FormikHelpers } from "formik";
import {accountService} from "../package/RestConnector"

interface Values {
  email: string;
  password: string;
}

const Home = () => {

  useEffect(() => {
    const data = accountService.getLoginUser()
    console.log('data',data);
    
  }, [])

  return (
    <div className="relative z-10 w-full h-full flex items-center justify-center">
      <Formik
        initialValues={{
          email: "",
          password: "",
        }}
        onSubmit={async(
          values: Values,
          { setSubmitting }: FormikHelpers<Values>
        ) => {
          const user = await accountService.loginAdmin({username:values.email,password:values.password}); 
          console.log('userrrrrrrrrrr',user);
          
          
        }}
      >
        <div className="w-full max-w-md">
          <Form className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            {/* <div className="w-full">
              <Image
                src={`/static/img/logo.png`}
                alt="logo-hcmute"
                objectFit="fill"
                width={350}
                height={250}
              />
            </div> */}
            <h1 className="text-gray-600 font-bold text-2xl text-center">Sign In</h1>
            <div className="mb-4">
              <label
                className="block text-gray-700 text-sm font-bold mb-2"
                htmlFor="email"
              >
                Email
              </label>
              <Field
                id="email"
                name="email"
                placeholder="example@hcmute.edu.vn"
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none focus: focus:border-blue-500 text-sm"
                type="email"
              />
            </div>
            <div className="mb-4">
              <label
                className="block text-gray-700 text-sm font-bold mb-2"
                htmlFor="password"
              >
                Password
              </label>
              <Field
                id="password"
                name="password"
                placeholder="********"
                className="appearance-none border-2 rounded-md w-full p-3 text-gray-700 leading-tight focus:outline-none focus: focus:border-blue-500 text-sm"
                type="password"
              />
            </div>
            <div className="flex items-center justify-between">
              <button
                className="bg-gradient-to-r from-green-400 to-blue-500 hover:from-pink-500 hover:to-yellow-500 text-white py-2 px-10 rounded focus:outline-none focus:shadow-outline font-semibold"
                type="submit"
              >
                Sign in
              </button>
              <a
                className="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
                href="#"
              >
                Forgot password?
              </a>
            </div>
            <div className="text-center text-sm mt-4 text-gray-500 font-medium">
              Don&#39;t have an account? <a href="#" className="text-transparent bg-clip-text bg-gradient-to-r from-green-400 to-blue-500 hover:from-pink-500 hover:to-yellow-500"> Sign up</a>
            </div>
          </Form>
        </div>
      </Formik>
    </div>
  );
};
Home.getLayout = function getLayout(page: ReactElement) {
  return <UserGuest>{page}</UserGuest>;
};
export default Home;
